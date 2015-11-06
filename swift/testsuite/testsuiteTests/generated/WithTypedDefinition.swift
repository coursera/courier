import Foundation
import SwiftyJSON

public struct WithTypedDefinition: Serializable, Equatable {
    
    public let value: TypedDefinition?
    
    public init(
        value: TypedDefinition?
    ) {
        self.value = value
    }
    
    public static func readJSON(json: JSON) throws -> WithTypedDefinition {
        return WithTypedDefinition(
            value: try json["value"].json.map { try TypedDefinition.readJSON($0) }
        )
    }
    public func writeData() -> [String: AnyObject] {
        var dict: [String : AnyObject] = [:]
        if let value = self.value {
            dict["value"] = value.writeData()
        }
        return dict
    }
}
public func ==(lhs: WithTypedDefinition, rhs: WithTypedDefinition) -> Bool {
    return (
        (lhs.value == nil ? (rhs.value == nil) : lhs.value! == rhs.value!) &&
        true
    )
}
