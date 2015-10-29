import Foundation
import SwiftyJSON

struct WithPrimitiveCustomTypes: JSONSerializable {
    
    let intField: Int?
    
    init(
        intField: Int?
    ) {
        self.intField = intField
    }
    
    static func read(json: JSON) -> WithPrimitiveCustomTypes {
        return WithPrimitiveCustomTypes(
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
