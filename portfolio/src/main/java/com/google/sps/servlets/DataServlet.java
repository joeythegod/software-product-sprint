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

package com.google.sps.servlets;

import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

import com.google.gson.Gson;


class Comment{
    private String name;
    private String content;
    Comment(String name, String content){
        this.name = name;
        this.content = content;
    }
} 


/** Servlet that returns some example content. TODO: modify this file to handle comments data */
@WebServlet("/data")
public class DataServlet extends HttpServlet {
  
  private List<Comment> comments = new ArrayList<>();

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    // response.setContentType("text/html;");
    // response.getWriter().println("Hello Joey!");
    // List<Comment> comments = new ArrayList<>();
    // Comment content1 = new Comment("Jack","Hi I am Jack");
    // Comment content2 = new Comment("John","Hi I am John");
    // Comment content3 = new Comment("Jimmy","Hi I am Jimmy");
    // comments.add(content1);
    // comments.add(content2);
    // comments.add(content3);
    String json = convertToJsonUsingGson(comments);

    // Send the JSON as the response
    response.setContentType("application/json;");
    response.getWriter().println(json);
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String name = getParameter(request, "text-input-1", "");
    String content = getParameter(request, "text-input-2", "");
    Comment comment = new Comment(name, content);
    comments.add(comment);
    response.sendRedirect("/index.html");
  }

    /**
   * Converts a ServerStats instance into a JSON string using the Gson library. Note: We first added
   * the Gson library dependency to pom.xml.
   */
  private String convertToJsonUsingGson(List<Comment> comments) {
    Gson gson = new Gson();
    String json = gson.toJson(comments);
    return json;
  }
  private String getParameter(HttpServletRequest request, String name, String defaultValue) {
    String value = request.getParameter(name);
    if (value == null) {
      return defaultValue;
    }
    return value;
  }
}


