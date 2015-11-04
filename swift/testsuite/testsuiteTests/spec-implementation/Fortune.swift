//
//  Fortune.swift
//  Bindings
//
//  Created by David Le on 10/12/15.
//  Copyright © 2015 David Le. All rights reserved.
//

import Foundation
import SwiftyJSON

/**
 * A fortune
 */
public struct Fortune: JSONSerializable, Equatable {
    
    /**
     * The fortune telling.
     */
    public let telling: Telling
    
    public let createdAt: DateTime
    
    // Example of a default:
    public init(telling: Telling, createdAt: DateTime = "2015-01-01T00:00:00.000Z") {
        self.telling = telling
        self.createdAt = createdAt
    }
    
    public static func read(json: JSON) -> Fortune {
        return Fortune(
            telling: Telling.read(json["telling"].jsonValue),
            createdAt: json["createdAt"].stringValue)
    }
    
    public func write() -> JSON {
        var json: [String : JSON] = [:]
        json["telling"] = self.telling.write()
        json["createdAt"] = JSON(self.createdAt)
        return JSON(json)
    }
    
    /* TODO: figure out how to properly implement
    var hashValue: Int {
        var hash = 1
        hash = hash * 17 + hashOf(self.telling)
        hash = hash * 17 + hashOf(self.createdAt)
        return hash
    }
    */
}

public func ==(lhs: Fortune, rhs: Fortune) -> Bool {
    return
        lhs.telling == rhs.telling &&
        lhs.createdAt == rhs.createdAt
}