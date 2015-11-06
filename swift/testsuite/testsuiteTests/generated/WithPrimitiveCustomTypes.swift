import Foundation
import SwiftyJSON

public struct WithPrimitiveCustomTypes: Serializable {
    
    public let intField: Int?
    
    public init(
        intField: Int?
    ) {
        self.intField = intField
    }
    
    public static func readJSON(json: JSON) throws -> WithPrimitiveCustomTypes {
        return WithPrimitiveCustomTypes(
            intField: json["intField"].int
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
