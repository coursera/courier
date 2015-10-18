//
//  FortuneCookie.swift
//  Bindings
//
//  Created by David Le on 10/12/15.
//  Copyright © 2015 David Le. All rights reserved.
//

import Foundation
import SwiftyJSON

/**
A fortune cookie.
*/
struct FortuneCookie: Equatable {
    
    /**
    A fortune cookie message.
    */
    let message: String
    
    let certainty: Float?
    
    let luckyNumbers: [Int]
    
    let map: [String: Int]
    
    let simpleArray: [Simple]?
    
    let simpleMap: [String: Simple]?
    
    let simple: Simple
    
    let simpleOpt: Simple?
    
    let arrayArray: [[Int]]?
    
    static func read(json: JSON) -> FortuneCookie {
        return FortuneCookie(
            message: json["message"].stringValue,
            certainty: json["certainty"].float,
            luckyNumbers: json["luckyNumbers"].arrayValue.map { $0.intValue },
            map: json["map"].dictionaryValue.mapValues { $0.intValue },
            simpleArray: json["simpleArray"].array.map { $0.map { Simple.read($0.jsonValue) } },
            simpleMap: json["simpleMap"].dictionary.map { $0.mapValues { Simple.read($0.jsonValue) } },
            simple: Simple.read(json["simple"].jsonValue),
            simpleOpt: json["simpleOpt"].json.map { Simple.read($0) },
            arrayArray: json["arrayArray"].array.map { $0.map { $0.arrayValue.map { $0.intValue }}})
    }
    
    func write() -> [String : JSON] {
        var json: [String : JSON] = [:]
        json["message"] = JSON(message)
        if let opt$ = certainty {
            json["certainty"] = JSON(opt$)
        }
        json["luckyNumbers"] = JSON(luckyNumbers)
        json["map"] = JSON(map)
        if let opt$ = simpleArray {
            json["simpleArray"] = JSON(opt$.map { JSON($0.write()) })
        }
        if let opt$ = simpleMap {
            json["simpleMap"] = JSON(opt$.mapValues { JSON($0.write()) })
        }
        json["simple"] = JSON(simple.write())
        if let opt$ = arrayArray {
            json["arrayArray"] = JSON(opt$.map { JSON($0.map { JSON($0) })})
        }
        return json
    }
    
    /* TODO: figure out how to properly implement
    var hashValue: Int {
        var hash = 1
        hash = hash * 17 + hashOf(self.message)
        hash = hash * 17 + hashOf(self.certainty)
        hash = hash * 17 + hashOf(self.luckyNumbers)
        hash = hash * 17 + hashOf(self.map)
        hash = hash * 17 + hashOf(self.simpleArray)
        hash = hash * 17 + hashOf(self.simpleMap)
        hash = hash * 17 + hashOf(self.simple)
        hash = hash * 17 + hashOf(self.simpleOpt)
        hash = hash * 17 + hashOf(self.arrayArray)
        return hash
    }
    */
}

/*
extension Simple: Equatable, Hashable {
    var hashValue: Int {
        if let message = self.message {
            return message.hashValue
        } else {
            return 0 // TODO: provide a reasonable hash for empty class case
        }
    }
}
func ==(lhs: Simple, rhs: Simple) -> Bool {
    return lhs.message == rhs.message
}*/

func ==(lhs: FortuneCookie, rhs: FortuneCookie) -> Bool {
    return
      lhs.message == rhs.message &&
      (lhs.certainty == nil ? (rhs.certainty == nil) : lhs.certainty! == rhs.certainty!) &&
      lhs.luckyNumbers == rhs.luckyNumbers &&
      lhs.map == rhs.map &&
      (lhs.simpleArray == nil ? (rhs.simpleArray == nil) : lhs.simpleArray! == rhs.simpleArray!) &&
      (lhs.simpleMap == nil ? (rhs.simpleMap == nil) : lhs.simpleMap! == rhs.simpleMap!) &&
      lhs.simple == rhs.simple &&
      lhs.simpleOpt == rhs.simpleOpt &&
      (lhs.arrayArray == nil ? (rhs.arrayArray == nil) : lhs.arrayArray! == rhs.arrayArray!)
}