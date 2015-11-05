import Foundation
import SwiftyJSON

@available(*, deprecated, message="Use XYZ instead")
public struct DeprecatedRecord: JSONSerializable, DataTreeSerializable {
    
    @available(*, deprecated)
    public let field1: String?
    
    @available(*, deprecated, message="Use XYZ instead")
    public let field2: String?
    
    public init(
        field1: String?,
        field2: String?
    ) {
        self.field1 = field1
        self.field2 = field2
    }
    
    public static func readJSON(json: JSON) -> DeprecatedRecord {
        return DeprecatedRecord(
            field1: json["field1"].string,
            field2: json["field2"].string
        )
    }
    public func writeJSON() -> JSON {
        return JSON(self.writeData())
    }
    public static func readData(data: [String: AnyObject]) -> DeprecatedRecord {
        return readJSON(JSON(data))
    }
    public func writeData() -> [String: AnyObject] {
        var dict: [String : AnyObject] = [:]
        if let field1 = self.field1 {
            dict["field1"] = field1
        }
        if let field2 = self.field2 {
            dict["field2"] = field2
        }
        return dict
    }
}
