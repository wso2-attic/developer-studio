/*
 * Copyright (c) 2016, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.wso2.developerstudio.eclipse.utils.file.model;

public class LineData {

	protected String line;
	protected String lineSeperator;

	public LineData(String line, String lineSeperator) {
		super();
		this.line = line;
		this.lineSeperator = lineSeperator;
	}
	
	public boolean isEmpty(){
		return line.isEmpty() && lineSeperator.isEmpty();
	}
	public int getFullLineLength(){
		return line.length() + lineSeperator.length();
	}

	public String getLine() {
		return line;
	}

	public void setLine(String line) {
		this.line = line;
	}

	public String getLineSeperator() {
		return lineSeperator;
	}

	public void setLineSeperator(String lineSeperator) {
		this.lineSeperator = lineSeperator;
	}

}
