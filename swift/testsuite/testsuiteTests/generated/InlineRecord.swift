import Foundation
import SwiftyJSON

public struct InlineRecord: JSONSerializable, DataTreeSerializable {
    
    public let value: Int?
    
    public init(
        value: Int?
    ) {
        self.value = value
    }
    
    public static func readJSON(json: JSON) throws -> InlineRecord {
        return InlineRecord(
            value: json["value"].int
        )
    }
    public func writeJSON() -> JSON {
        return JSON(self.writeData())
    }
    public static func readData(data: [String: AnyObject]) throws -> InlineRecord {
        return try readJSON(JSON(data))
    }
    public func writeData() -> [String: AnyObject] {
        var dict: [String : AnyObject] = [:]
        if let value = self.value {
            dict["value"] = value
        }
        return dict
    }
}
