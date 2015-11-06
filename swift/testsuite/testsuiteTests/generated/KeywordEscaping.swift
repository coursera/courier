import Foundation
import SwiftyJSON

public struct KeywordEscaping: Serializable {
    
    public let `default`: String?
    
    public init(
        `default`: String?
    ) {
        self.`default` = `default`
    }
    
    public static func readJSON(json: JSON) throws -> KeywordEscaping {
        return KeywordEscaping(
            `default`: json["default"].string
        )
    }
    public func writeData() -> [String: AnyObject] {
        var dict: [String : AnyObject] = [:]
        if let `default` = self.`default` {
            dict["default"] = `default`
        }
        return dict
    }
}
