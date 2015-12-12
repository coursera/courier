import Foundation
import SwiftyJSON

public struct KeywordEscaping: Serializable {
    
    public let type: String?
    
    public init(
        type: String?
    ) {
        self.type = type
    }
    
    public static func readJSON(json: JSON) throws -> KeywordEscaping {
        return KeywordEscaping(
            type: try json["type"].optional(.String).string
        )
    }
    public func writeData() -> [String: AnyObject] {
        var dict: [String : AnyObject] = [:]
        if let type = self.type {
            dict["type"] = type
        }
        return dict
    }
}
