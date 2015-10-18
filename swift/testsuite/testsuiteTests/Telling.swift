//
//  Telling.swift
//  Bindings
//
//  Created by David Le on 10/12/15.
//  Copyright © 2015 David Le. All rights reserved.
//

import Foundation
import SwiftyJSON

enum Telling: Equatable {
    case FortuneCookieType(FortuneCookie)
    case MagicEightBallType(MagicEightBall)
    case StringType(String)
    case UNKNOWN$([String : JSON])
    
    static func read(json: JSON) -> Telling {
        let dictionary = json.dictionaryValue
        if let member = dictionary["org.example.FortuneCookie"] {
            return .FortuneCookieType(FortuneCookie.read(member.jsonValue))
        }
        if let member = dictionary["org.example.MagicEightBall"] {
            return .MagicEightBallType(MagicEightBall.read(member.jsonValue))
        }
        if let member = dictionary["string"] {
            return .StringType(member.stringValue)
        }
        return .UNKNOWN$(dictionary)
    }
    
    func write() -> [String : JSON] {
        switch self {
        case .FortuneCookieType(let member):
            return ["org.example.FortuneCookie": JSON(member.write())]
        case .MagicEightBallType(let member):
            return ["org.example.MagicEightBall": JSON(member.write())]
        case .StringType(let member):
            return ["string": JSON(member)]
        case .UNKNOWN$(let dictionary):
            return dictionary
        }
    }
    
    /* TODO: figure out how to properly implement
    var hashValue: Int {
        switch self {
        case .FortuneCookieType(let member):
            return hashOf(member)
        case .MagicEightBallType(let member):
            return hashOf(member)
        case .StringType(let member):
            return hashOf(member)
        case .UNKNOWN$(let dictionary):
            return hashOf(dictionary)
        }
    }
    */
}

func ==(lhs: Telling, rhs: Telling) -> Bool {
    switch (lhs, rhs) {
    case (let .FortuneCookieType(lhs), let .FortuneCookieType(rhs)):
        return lhs == rhs
    case (let .MagicEightBallType(lhs), let .MagicEightBallType(rhs)):
        return lhs == rhs
    case (let .StringType(lhs), let .StringType(rhs)):
        return lhs == rhs
    case (let .UNKNOWN$(lhs), let .UNKNOWN$(rhs)):
        return lhs == rhs
    default:
        return false
    }
}