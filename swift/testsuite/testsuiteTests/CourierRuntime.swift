//
//  CourierRuntime.swift
//  testsuite
//
//  Created by Joe Betz on 10/26/15.
//  Copyright © 2015 Joe Betz. All rights reserved.
//

import Foundation
import SwiftyJSON

extension Dictionary {
    init(_ elements: [Element]){
        self.init()
        for (k, v) in elements {
            self[k] = v
        }
    }
    
    func mapValues<U>(transform: Value -> U) -> [Key : U] {
        return Dictionary<Key, U>(self.map{ (key, value) in (key, transform(value)) })
    }
}

extension JSON {
    
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

/* TODO: figure out how to properly hash arrays and maps, the below is no good
func hashOf<T: Hashable>(array: [T]) -> Int {
    var hash = 1
    for e in array {
        hash = hash * 17 + e.hashValue
    }
    return hash
}

func hashOf<T: Hashable>(array: [T]?) -> Int {
    if let array = array {
        return hashOf(array)
    } else {
        return 0
    }
}

func hashOf<K: Hashable, V: Hashable>(dictionary: [K: V]) -> Int {
    var hash = 1
    for (k, v) in dictionary {
        hash = hash * 17 + k.hashValue
        hash = hash * 17 + v.hashValue
    }
    return hash
}

func hashOf<K: Hashable, V: Hashable>(dictionary: [K: V]?) -> Int {
    if let dictionary = dictionary {
        return hashOf(dictionary)
    } else {
        return 0
    }
}

func hashOf<E: Hashable>(value: E?) -> Int {
    if let value = value {
       return hashOf(value)
    } else {
        return 0
    }
}

func hashOf<E: Hashable>(value: E) -> Int {
    return value.hashValue
}

func hashOf(dictionary: [String: JSON]) -> Int {
    var hash = 1
    for (k, v) in dictionary {
        hash = hash * 17 + k.hashValue
        hash = hash * 17 + v.object.hashValue
    }
    return hash
}
*/

// arrays of arrays
func ==<T: Equatable>(lhs: [[T]], rhs: [[T]]) -> Bool {
    for (var i = 0; i < lhs.count; i++) {
        if (lhs[i] != rhs[i]) {
            return false
        }
    }
    return true
}

// arrays of dictionaries
func ==<K, V: Equatable>(lhs: [[K:V]], rhs: [[K:V]]) -> Bool {
    for (var i = 0; i < lhs.count; i++) {
        if (lhs[i] != rhs[i]) {
            return false
        }
    }
    return true
}

// dictionaries of dictionaries
func ==<K, K2, V: Equatable>(lhs: [K:[K2:V]], rhs: [K:[K2:V]]) -> Bool {
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
func ==<K, E: Equatable>(lhs: [K:[E]], rhs: [K:[E]]) -> Bool {
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
func ==<K, E: Equatable>(lhs: [K:[[E]]], rhs: [K:[[E]]]) -> Bool {
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
