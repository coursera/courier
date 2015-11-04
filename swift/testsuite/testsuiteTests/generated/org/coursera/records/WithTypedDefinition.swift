import Foundation
import SwiftyJSON

public struct WithTypedDefinition: JSONSerializable, Equatable {
    
    public let value: TypedDefinition?
    
    public init(
        value: TypedDefinition?
    ) {
        self.value = value
    }
    
    public static func read(json: JSON) -> WithTypedDefinition {
        return WithTypedDefinition(
            value: json["value"].json.map { TypedDefinition.read($0) }
        )
    }
    public func write() -> JSON {
        var json: [String : JSON] = [:]
        if let value = self.value {
            json["value"] = value.write()
        }
        return JSON(json)
    }
}
public func ==(lhs: WithTypedDefinition, rhs: WithTypedDefinition) -> Bool {
    return (
        (lhs.value == nil ? (rhs.value == nil) : lhs.value! == rhs.value!) &&
        true
    )
}
