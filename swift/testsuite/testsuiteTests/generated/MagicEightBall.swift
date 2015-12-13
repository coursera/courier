import Foundation
import SwiftyJSON

public struct MagicEightBall: Serializable {
    
    public let question: String?
    
    public let answer: MagicEightBallAnswer?
    
    public init(
        question: String?,
        answer: MagicEightBallAnswer?
    ) {
        self.question = question
        self.answer = answer
    }
    
    public static func readJSON(json: JSON) throws -> MagicEightBall {
        return MagicEightBall(
            question: try json["question"].optional(.String).string,
            answer: try json["answer"].optional(.String).string.map { MagicEightBallAnswer.read($0) }
        )
    }
    public func writeData() -> [String: AnyObject] {
        var dict: [String : AnyObject] = [:]
        if let question = self.question {
            dict["question"] = question
        }
        if let answer = self.answer {
            dict["answer"] = answer.write()
        }
        return dict
    }
}
