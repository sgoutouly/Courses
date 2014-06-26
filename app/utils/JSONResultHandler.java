/*
 * Copyright (C) 2011 Benoit GUEROUT <bguerout at gmail dot com> and Yves AMSELLEM <amsellem dot yves at gmail dot com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package utils;

import com.mongodb.DBObject;
import com.mongodb.util.JSONSerializers;
import org.jongo.ResultHandler;

public class JSONResultHandler implements ResultHandler<String> {
    public String map(DBObject result) {
        return JSONSerializers.getStrict().serialize(result);
    }


    public static String jsonify(String json) {
        return json.replace("'", "\"");
    }
}