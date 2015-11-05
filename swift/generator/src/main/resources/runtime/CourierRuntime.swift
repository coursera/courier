//
// Copyright 2015 Coursera Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//

import Foundation
import SwiftyJSON

/**
  Serializable to and from JSON via SwiftyJson.
*/
public protocol JSONSerializable: JSONReadable, JSONWritable {}

/**
  Readable from JSON via SwiftyJson.
*/
public protocol JSONReadable {
    static func readJSON(json: JSON) -> Self
}

/**
  Writable to JSON via SwiftyJson.
*/
public protocol JSONWritable {
    func writeJSON() -> JSON
}

/**
  Readable and writable as a "DataTree".

  a "DataTree" is a "JSON equivalent" data structure composed of the following
  Swift types:

  * `[String: AnyObject]` - Equivalent to a JSON object.
  * `[AnyObject]` - Equivalent to a JSON array.
  * `String`, `Boolean` and numeric types. - Equivalent their respective JSON primitive types.

  Structs and classes are represented as a "DataTree" by an `[String: AnyObject]` map where
  field names used as map keys. If a optional field is `nil` valued it is simply left absent
  from the map.
*/
public protocol DataTreeSerializable: DataTreeReadable, DataTreeWritable {}

/**
  Readable from a "DataTree".
*/
public protocol DataTreeReadable {
  static func readData(data: [String: AnyObject]) -> Self
}

/**
  Writable to a "DataTree".
*/
public protocol DataTreeWritable {
  func writeData() -> [String: AnyObject]
}

public extension Dictionary {
    public init(_ elements: [Element]){
        self.init()
        for (k, v) in elements {
            self[k] = v
        }
    }

    public func mapValues<U>(transform: Value -> U) -> [Key : U] {
        return Dictionary<Key, U>(self.map{ (key, value) in (key, transform(value)) })
    }
}

public extension JSON {

    //Optional JSON
    public var json: JSON? {
        get {
            switch self.type {
            case .Null:
                return nil
            default:
                return self
            }
        }
        set {
            if let value = newValue {
                self = value
            } else {
                self = nil
            }
        }
    }

    // Not needed, but added to maintain SwiftyJSON consistency.
    // SwiftyJSON expects a corresponding "xyzValue" accessor for all "xyz" accessors.
    public var jsonValue: JSON {
        get {
            return self
        }
        set {
            self = newValue
        }
    }
}

// arrays of arrays
public func ==<T: Equatable>(lhs: [[T]], rhs: [[T]]) -> Bool {
    for (var i = 0; i < lhs.count; i++) {
        if (lhs[i] != rhs[i]) {
            return false
        }
    }
    return true
}

// arrays of dictionaries
public func ==<K, V: Equatable>(lhs: [[K:V]], rhs: [[K:V]]) -> Bool {
    for (var i = 0; i < lhs.count; i++) {
        if (lhs[i] != rhs[i]) {
            return false
        }
    }
    return true
}

// dictionaries of dictionaries
public func ==<K, K2, V: Equatable>(lhs: [K:[K2:V]], rhs: [K:[K2:V]]) -> Bool {
    if (lhs.count != rhs.count) {
        return false
    }
    for (k, v) in lhs {
        if let rhsv = rhs[k] {
            if (v != rhsv) {
                return false
            }
        } else {
            return false
        }
    }
    return true
}

// dictionaries of arrays
public func ==<K, E: Equatable>(lhs: [K:[E]], rhs: [K:[E]]) -> Bool {
    if (lhs.count != rhs.count) {
        return false
    }
    for (k, v) in lhs {
        if let rhsv = rhs[k] {
            if (v != rhsv) {
                return false
            }
        } else {
            return false
        }
    }
    return true
}

// dictionaries of arrays of arrays
public func ==<K, E: Equatable>(lhs: [K:[[E]]], rhs: [K:[[E]]]) -> Bool {
    if (lhs.count != rhs.count) {
        return false
    }
    for (k, v) in lhs {
        if let rhsv = rhs[k] {
            if (!(v == rhsv)) {
                return false
            }
        } else {
            return false
        }
    }
    return true
}
