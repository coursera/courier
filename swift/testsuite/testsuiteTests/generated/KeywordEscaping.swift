import Foundation
import SwiftyJSON

public struct KeywordEscaping: JSONSerializable, DataTreeSerializable {
    
    public let `default`: String?
    
    public init(
        `default`: String?
    ) {
        self.`default` = `default`
    }
    
    public static func readJSON(json: JSON) -> KeywordEscaping {
        return KeywordEscaping(
            `default`: json["default"].string
        )
    }
    public func writeJSON() -> JSON {
        return JSON(self.writeData())
    }
    public static func readData(data: [String: AnyObject]) -> KeywordEscaping {
        return readJSON(JSON(data))
    }
    public func writeData() -> [String: AnyObject] {
        var dict: [String : AnyObject] = [:]
        if let `default` = self.`default` {
            dict["default"] = `default`
        }
        return dict
    }
}
