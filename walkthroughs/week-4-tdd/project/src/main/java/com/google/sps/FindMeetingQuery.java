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
    Collection<TimeRange> freeTimeRange = Arrays.asList(TimeRange.WHOLE_DAY);
    if (attendees.size() == 0){
      return freeTimeRange;
    }
    if (duration > TimeRange.WHOLE_DAY.duration()){
      return Arrays.asList();
    }
    
    for (Event e: events){
      Collection<TimeRange> tmp = new ArrayList<TimeRange>();
      TimeRange meeting = e.getWhen();
      for (TimeRange t: freeTimeRange){
        if (t.overlaps(meeting)){
          System.out.print(t);
          TimeRange t1 = TimeRange.fromStartEnd(t.start(), meeting.start(), false);
          TimeRange t2 = TimeRange.fromStartEnd(meeting.end(), t.end(), false);
          tmp.add(t1);
          tmp.add(t2);
        }
      freeTimeRange = tmp;
      }
    }
    return freeTimeRange;
  }
}
