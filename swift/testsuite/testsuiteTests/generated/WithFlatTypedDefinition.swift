import Foundation
import SwiftyJSON

public struct WithFlatTypedDefinition: Serializable, Equatable {
    
    public let value: FlatTypedDefinition?
    
    public init(
        value: FlatTypedDefinition?
    ) {
        self.value = value
    }
    
    public static func readJSON(json: JSON) throws -> WithFlatTypedDefinition {
        return WithFlatTypedDefinition(
            value: try json["value"].optional(.Dictionary).json.map { try FlatTypedDefinition.readJSON($0) }
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
public func ==(lhs: WithFlatTypedDefinition, rhs: WithFlatTypedDefinition) -> Bool {
    return (
        (lhs.value == nil ? (rhs.value == nil) : lhs.value! == rhs.value!) &&
        true
    )
}
