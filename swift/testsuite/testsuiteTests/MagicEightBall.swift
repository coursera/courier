//
//  MagicEightBall.swift
//  Bindings
//
//  Created by David Le on 10/12/15.
//  Copyright © 2015 David Le. All rights reserved.
//

import Foundation
import SwiftyJSON

struct MagicEightBall: Equatable {
    
    let question: String
    let answer: MagicEightBallAnswer
    
    init(question: String, answer: MagicEightBallAnswer) {
        self.question = question
        self.answer = answer
    }
    
    static func read(json: JSON) -> MagicEightBall {
        return MagicEightBall(
            question: json["question"].stringValue,
            answer: MagicEightBallAnswer.read(json["answer"].stringValue))
    }
    
    func write() -> [String : JSON] {
        var json: [String : JSON] = [:]
        json["question"] = JSON(self.question)
        json["answer"] = JSON(self.answer.write())
        return json
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

func ==(lhs: MagicEightBall, rhs: MagicEightBall) -> Bool {
    return
        lhs.question == rhs.question &&
        lhs.answer == rhs.answer
}