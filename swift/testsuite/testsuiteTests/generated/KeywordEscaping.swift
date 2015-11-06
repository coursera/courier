import Foundation
import SwiftyJSON

public struct KeywordEscaping: JSONSerializable, DataTreeSerializable {
    
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
    public func writeJSON() -> JSON {
        return JSON(self.writeData())
    }
    public static func readData(data: [String: AnyObject]) throws -> KeywordEscaping {
        return try readJSON(JSON(data))
    }
    public func writeData() -> [String: AnyObject] {
        var dict: [String : AnyObject] = [:]
        if let `default` = self.`default` {
            dict["default"] = `default`
        }
        return dict
    }
}
