import Foundation
import SwiftyJSON

public struct WithoutNamespace: JSONSerializable, DataTreeSerializable {
    
    public let field1: String?
    
    public init(
        field1: String?
    ) {
        self.field1 = field1
    }
    
    public static func readJSON(json: JSON) -> WithoutNamespace {
        return WithoutNamespace(
            field1: json["field1"].string
        )
    }
    public func writeJSON() -> JSON {
        return JSON(self.writeData())
    }
    public static func readData(data: [String: AnyObject]) -> WithoutNamespace {
        return readJSON(JSON(data))
    }
    public func writeData() -> [String: AnyObject] {
        var dict: [String : AnyObject] = [:]
        if let field1 = self.field1 {
            dict["field1"] = field1
        }
        return dict
    }
}
