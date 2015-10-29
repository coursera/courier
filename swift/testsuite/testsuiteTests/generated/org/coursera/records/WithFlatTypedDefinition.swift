import Foundation
import SwiftyJSON

struct WithFlatTypedDefinition: JSONSerializable, Equatable {
    
    let value: FlatTypedDefinition?
    
    init(
        value: FlatTypedDefinition?
    ) {
        self.value = value
    }
    
    static func read(json: JSON) -> WithFlatTypedDefinition {
        return WithFlatTypedDefinition(
            value: json["value"].json.map { FlatTypedDefinition.read($0) }
        )
    }
    func write() -> JSON {
        var json: [String : JSON] = [:]
        if let value = self.value {
            json["value"] = value.write()
        }
        return JSON(json)
    }
}
func ==(lhs: WithFlatTypedDefinition, rhs: WithFlatTypedDefinition) -> Bool {
    return (
        (lhs.value == nil ? (rhs.value == nil) : lhs.value! == rhs.value!) &&
        true
    )
}
