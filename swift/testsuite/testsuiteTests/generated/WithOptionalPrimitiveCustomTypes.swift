import Foundation
import SwiftyJSON

public struct WithOptionalPrimitiveCustomTypes: Serializable {
    
    public let intField: Int?
    
    public init(
        intField: Int? = nil
    ) {
        self.intField = intField
    }
    
    public static func readJSON(json: JSON) throws -> WithOptionalPrimitiveCustomTypes {
        return WithOptionalPrimitiveCustomTypes(
            intField: try json["intField"].optional(.Number).int
        )
    }
    public func writeData() -> [String: AnyObject] {
        var dict: [String : AnyObject] = [:]
        if let intField = self.intField {
            dict["intField"] = intField
        }
        return dict
    }
}
