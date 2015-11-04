import Foundation
import SwiftyJSON

public struct WithOptionalPrimitiveCustomTypes: JSONSerializable {
    
    public let intField: Int?
    
    public init(
        intField: Int?
    ) {
        self.intField = intField
    }
    
    public static func read(json: JSON) -> WithOptionalPrimitiveCustomTypes {
        return WithOptionalPrimitiveCustomTypes(
            intField: json["intField"].int
        )
    }
    public func write() -> JSON {
        var json: [String : JSON] = [:]
        if let intField = self.intField {
            json["intField"] = JSON(intField)
        }
        return JSON(json)
    }
}
