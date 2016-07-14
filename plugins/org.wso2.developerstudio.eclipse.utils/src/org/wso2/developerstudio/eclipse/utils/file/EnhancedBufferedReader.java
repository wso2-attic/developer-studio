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

package org.wso2.developerstudio.eclipse.utils.file;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

import org.wso2.developerstudio.eclipse.utils.file.model.LineData;

public class EnhancedBufferedReader extends BufferedReader {

	private static final char CARRIAGE_RETURN = '\r';
	private static final char LINE_FEED = '\n';

	public EnhancedBufferedReader(Reader in) {
		super(in);
	}

	/**
	 * An enhanced method - alternative to readLine - which reads new line chars
	 * as well
	 * 
	 * @return
	 * @throws IOException
	 */
	public LineData readLineData() throws IOException {
		String lineSeperator = "";
		StringBuffer lineBuffer = new StringBuffer();

		int nextChar;
		while ((nextChar = read()) != -1) {
			if (nextChar == LINE_FEED) {
				lineSeperator += (char) nextChar;
				break;
			} else if (nextChar == CARRIAGE_RETURN) {
				lineSeperator += (char) nextChar;
				continue;
			}
			lineBuffer.append((char)nextChar);
		}
		return new LineData(lineBuffer.toString(), lineSeperator);
	}
}
