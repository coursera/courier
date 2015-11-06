import Foundation
import SwiftyJSON

public struct WithOptionalPrimitiveCustomTypes: JSONSerializable, DataTreeSerializable {
    
    public let intField: Int?
    
    public init(
        intField: Int?
    ) {
        self.intField = intField
    }
    
    public static func readJSON(json: JSON) throws -> WithOptionalPrimitiveCustomTypes {
        return WithOptionalPrimitiveCustomTypes(
            intField: json["intField"].int
        )
    }
    public func writeJSON() -> JSON {
        return JSON(self.writeData())
    }
    public static func readData(data: [String: AnyObject]) throws -> WithOptionalPrimitiveCustomTypes {
        return try readJSON(JSON(data))
    }
    public func writeData() -> [String: AnyObject] {
        var dict: [String : AnyObject] = [:]
        if let intField = self.intField {
            dict["intField"] = intField
        }
        return dict
    }
}
