import Foundation
import SwiftyJSON

public struct `class`: JSONSerializable, DataTreeSerializable {
    
    public let `private`: String?
    
    public init(
        `private`: String?
    ) {
        self.`private` = `private`
    }
    
    public static func readJSON(json: JSON) throws -> `class` {
        return `class`(
            `private`: json["private"].string
        )
    }
    public func writeJSON() -> JSON {
        return JSON(self.writeData())
    }
    public static func readData(data: [String: AnyObject]) throws -> `class` {
        return try readJSON(JSON(data))
    }
    public func writeData() -> [String: AnyObject] {
        var dict: [String : AnyObject] = [:]
        if let `private` = self.`private` {
            dict["private"] = `private`
        }
        return dict
    }
}
