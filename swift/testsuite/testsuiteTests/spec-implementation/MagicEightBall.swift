//
//  MagicEightBall.swift
//  Bindings
//
//  Created by David Le on 10/12/15.
//  Copyright © 2015 David Le. All rights reserved.
//

import Foundation
import SwiftyJSON

public struct MagicEightBall: JSONSerializable, Equatable {
    
    public let question: String
    public let answer: MagicEightBallAnswer
    
    public init(question: String, answer: MagicEightBallAnswer) {
        self.question = question
        self.answer = answer
    }
    
    public static func read(json: JSON) -> MagicEightBall {
        return MagicEightBall(
            question: json["question"].stringValue,
            answer: MagicEightBallAnswer.read(json["answer"].stringValue))
    }
    
    public func write() -> JSON {
        var json: [String : JSON] = [:]
        json["question"] = JSON(self.question)
        json["answer"] = JSON(self.answer.write())
        return JSON(json)
    }
    
    /* TODO: figure out how to properly implement
    var hashValue: Int {
        var hash = 1
        hash = hash * 17 + question.hashValue
        hash = hash * 17 + answer.hashValue
        return hash
    }
    */
}

public func ==(lhs: MagicEightBall, rhs: MagicEightBall) -> Bool {
    return
        lhs.question == rhs.question &&
        lhs.answer == rhs.answer
}