package util;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import java.io.IOException;
import java.io.PushbackReader;
import java.io.Reader;

/**
 * Reader transforming unicode escape sequences (i.e \u0065) in the provided
 * stream into the corresponding unicode character.
 *
 * @author Emmanuel Bourg
 * @version $Revision: 1300540 $, $Date: 2012-03-14 13:38:43 +0000 (Wed, 14 Mar 2012) $
 */
public class UnicodeUnescapeReader extends Reader {
    private final PushbackReader reader;

    /** The buffer used to read unicode escape sequences. */
    private final char[] sequence = new char[5];

    public UnicodeUnescapeReader(Reader reader) {
        this.reader = new PushbackReader(reader, sequence.length);
    }

    @Override
    public int read(char[] cbuf, int offset, int length) throws IOException {
        int count = 0;
        for (int i = 0; i < length; i++) {
            int c = reader.read();

            if (c == -1) {
                return count == 0 ? -1 : count;
            }

            if (c == '\\') {
                int len = reader.read(sequence);
                if (len == sequence.length && isUnicodeSequence(sequence)) {
                    // unicode escape found
                    c = Integer.parseInt(new String(sequence, 1, 4), 16);

                } else if (len > 0) {
                    // put the characters back in the stream
                    reader.unread(sequence, 0, len);
                }
            }

            cbuf[offset + i] = (char) c;
            count++;
        }

        return count;
    }

    private boolean isUnicodeSequence(char[] sequence) {
        return 'u' == sequence[0]
                && isHexadecimal(sequence[1])
                && isHexadecimal(sequence[2])
                && isHexadecimal(sequence[3])
                && isHexadecimal(sequence[4]);
    }

    private boolean isHexadecimal(char c) {
        return ('0' <= c && c <= '9') || ('a' <= c && c <= 'f') || ('A' <= c && c <= 'F');
    }

    @Override
    public void close() throws IOException {
        if (reader != null) {
            reader.close();
        }
    }
}