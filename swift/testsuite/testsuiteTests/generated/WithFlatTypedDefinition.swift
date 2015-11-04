import Foundation
import SwiftyJSON

public struct WithFlatTypedDefinition: JSONSerializable, Equatable {
    
    public let value: FlatTypedDefinition?
    
    public init(
        value: FlatTypedDefinition?
    ) {
        self.value = value
    }
    
    public static func read(json: JSON) -> WithFlatTypedDefinition {
        return WithFlatTypedDefinition(
            value: json["value"].json.map { FlatTypedDefinition.read($0) }
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
public func ==(lhs: WithFlatTypedDefinition, rhs: WithFlatTypedDefinition) -> Bool {
    return (
        (lhs.value == nil ? (rhs.value == nil) : lhs.value! == rhs.value!) &&
        true
    )
}
