import Foundation
import SwiftyJSON

struct WithOptionalPrimitiveCustomTypes: JSONSerializable {
    
    let intField: Int?
    
    init(
        intField: Int?
    ) {
        self.intField = intField
    }
    
    static func read(json: JSON) -> WithOptionalPrimitiveCustomTypes {
        return WithOptionalPrimitiveCustomTypes(
            intField: json["intField"].int
        )
    }
    func write() -> JSON {
        var json: [String : JSON] = [:]
        if let intField = self.intField {
            json["intField"] = JSON(intField)
        }
        return JSON(json)
    }
}
