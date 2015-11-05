import Foundation
import SwiftyJSON

public struct InlineOptionalRecord: JSONSerializable, DataTreeSerializable {
    
    public let value: String?
    
    public init(
        value: String?
    ) {
        self.value = value
    }
    
    public static func readJSON(json: JSON) -> InlineOptionalRecord {
        return InlineOptionalRecord(
            value: json["value"].string
        )
    }
    public func writeJSON() -> JSON {
        return JSON(self.writeData())
    }
    public static func readData(data: [String: AnyObject]) -> InlineOptionalRecord {
        return readJSON(JSON(data))
    }
    public func writeData() -> [String: AnyObject] {
        var dict: [String : AnyObject] = [:]
        if let value = self.value {
            dict["value"] = value
        }
        return dict
    }
}
