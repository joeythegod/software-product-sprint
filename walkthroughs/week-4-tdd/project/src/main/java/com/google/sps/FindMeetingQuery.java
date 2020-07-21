// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps;

import java.util.*;


public final class FindMeetingQuery {

  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {

    Collection<String> attendees = request.getAttendees();
    long duration = request.getDuration();
    List<TimeRange> freeTimeRange = Arrays.asList(TimeRange.WHOLE_DAY);
    ArrayList<TimeRange> possibleTimeRange = new ArrayList<>();

    // no attendees
    if (attendees.size() == 0){
      return freeTimeRange;
    }

    // impossible arrange a meeting
    if (duration > TimeRange.WHOLE_DAY.duration()){
      return Arrays.asList();
    }
    
    // split time range by meeting
    for (Event event: events){
      Set<String> requiredAttendees = new HashSet<>(event.getAttendees());
      requiredAttendees.retainAll(attendees);
      if (!requiredAttendees.isEmpty()){
        freeTimeRange = splitTimeRange(freeTimeRange, event.getWhen());
      }
    }

    // check duration is enough
    for (TimeRange range: freeTimeRange){
      if (range.duration() >= duration){
        possibleTimeRange.add(range);
      }
    }
    return possibleTimeRange;
  }

  public List<TimeRange> splitTimeRange(Collection<TimeRange> freeTimeRange, TimeRange meeting){
    ArrayList<TimeRange> ranges = new ArrayList<>(freeTimeRange);
    int idx = 0;
    for (TimeRange range: ranges){
      if (range.overlaps(meeting)){
        // process the flag
        boolean meetingStartEarlier = true;
        boolean meetingEndEarlier = true;
        if (range.start() < meeting.start()){
          meetingStartEarlier = false;
        }
        if (range.end() <= meeting.end()){
          meetingEndEarlier = false;
        }

        // split case by case
        // Case 1
        //    |-----| range
        // |-----|    meeting
        if (meetingStartEarlier && meetingEndEarlier){
          TimeRange free = TimeRange.fromStartEnd(meeting.end(), range.end(), false);
          ranges.set(idx, free);
        }
        // Case 2
        // |-----|    range
        //    |-----| meeting
        else if (!meetingStartEarlier && !meetingEndEarlier){
          TimeRange free = TimeRange.fromStartEnd(range.start(), meeting.start(), false);
          ranges.set(idx, free);
        }
        // Case 3
        //   |---|    range
        // |-------|  meeting
        else if (meetingStartEarlier && !meetingEndEarlier){
          ranges.remove(idx);
          idx = idx - 1;
        }
        // Case 4
        // |-------|  range
        //   |---|    meeting
        else {
          TimeRange freeLeft = TimeRange.fromStartEnd(range.start(), meeting.start(), false);
          TimeRange freeRight = TimeRange.fromStartEnd(meeting.end(), range.end(), false);
          ranges.set(idx, freeLeft);
          ranges.add(idx + 1, freeRight);
          break;
        }
      }
      idx = idx + 1;
    }
    return ranges;
  }
}
