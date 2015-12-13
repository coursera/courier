import Foundation
import SwiftyJSON

/**
    A fortune cookie.
*/
public struct FortuneCookie: Serializable {
    
    /**
        A fortune cookie message.
    */
    public let message: String?
    
    public let certainty: Float?
    
    public let luckyNumbers: [Int]?
    
    public init(
        message: String?,
        certainty: Float? = nil,
        luckyNumbers: [Int]?
    ) {
        self.message = message
        self.certainty = certainty
        self.luckyNumbers = luckyNumbers
    }
    
    public static func readJSON(json: JSON) throws -> FortuneCookie {
        return FortuneCookie(
            message: try json["message"].optional(.String).string,
            certainty: try json["certainty"].optional(.Number).float,
            luckyNumbers: try json["luckyNumbers"].optional(.Array).array.map { try $0.map { try $0.required(.Number).intValue } }
        )
    }
    public func writeData() -> [String: AnyObject] {
        var dict: [String : AnyObject] = [:]
        if let message = self.message {
            dict["message"] = message
        }
        if let certainty = self.certainty {
            dict["certainty"] = certainty
        }
        if let luckyNumbers = self.luckyNumbers {
            dict["luckyNumbers"] = luckyNumbers
        }
        return dict
    }
}
