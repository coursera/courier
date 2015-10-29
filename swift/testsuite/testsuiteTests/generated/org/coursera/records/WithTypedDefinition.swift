import Foundation
import SwiftyJSON

struct WithTypedDefinition: JSONSerializable, Equatable {
    
    let value: TypedDefinition?
    
    init(
        value: TypedDefinition?
    ) {
        self.value = value
    }
    
    static func read(json: JSON) -> WithTypedDefinition {
        return WithTypedDefinition(
            value: json["value"].json.map { TypedDefinition.read($0) }
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
func ==(lhs: WithTypedDefinition, rhs: WithTypedDefinition) -> Bool {
    return (
        (lhs.value == nil ? (rhs.value == nil) : lhs.value! == rhs.value!) &&
        true
    )
}
