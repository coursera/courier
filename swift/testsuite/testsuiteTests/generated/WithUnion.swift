import Foundation
import SwiftyJSON

public struct WithUnion: Serializable, Equatable {
    
    public let value: Union?
    
    public init(
        value: Union?
    ) {
        self.value = value
    }
    
    public static func readJSON(json: JSON) throws -> WithUnion {
        return WithUnion(
            value: try json["value"].optional(.Dictionary).json.map {try Union.readJSON($0) }
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
public func ==(lhs: WithUnion, rhs: WithUnion) -> Bool {
    return (
        (lhs.value == nil ? (rhs.value == nil) : lhs.value! == rhs.value!) &&
        true
    )
}
