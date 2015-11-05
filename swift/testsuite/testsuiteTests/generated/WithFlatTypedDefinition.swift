import Foundation
import SwiftyJSON

public struct WithFlatTypedDefinition: JSONSerializable, DataTreeSerializable, Equatable {
    
    public let value: FlatTypedDefinition?
    
    public init(
        value: FlatTypedDefinition?
    ) {
        self.value = value
    }
    
    public static func readJSON(json: JSON) -> WithFlatTypedDefinition {
        return WithFlatTypedDefinition(
            value: json["value"].json.map { FlatTypedDefinition.readJSON($0) }
        )
    }
    public func writeJSON() -> JSON {
        return JSON(self.writeData())
    }
    public static func readData(data: [String: AnyObject]) -> WithFlatTypedDefinition {
        return readJSON(JSON(data))
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
