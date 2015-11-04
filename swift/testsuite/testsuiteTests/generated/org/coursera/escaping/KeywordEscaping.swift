import Foundation
import SwiftyJSON

public struct KeywordEscaping: JSONSerializable {
    
    public let `default`: String?
    
    public init(
        `default`: String?
    ) {
        self.`default` = `default`
    }
    
    public static func read(json: JSON) -> KeywordEscaping {
        return KeywordEscaping(
            `default`: json["default"].string
        )
    }
    public func write() -> JSON {
        var json: [String : JSON] = [:]
        if let `default` = self.`default` {
            json["default"] = JSON(`default`)
        }
        return JSON(json)
    }
}
