import Foundation
import SwiftyJSON

public struct WithUnion: JSONSerializable, Equatable {
    
    public let value: Union?
    
    public init(
        value: Union?
    ) {
        self.value = value
    }
    
    public static func read(json: JSON) -> WithUnion {
        return WithUnion(
            value: json["value"].json.map { Union.read($0) }
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
public func ==(lhs: WithUnion, rhs: WithUnion) -> Bool {
    return (
        (lhs.value == nil ? (rhs.value == nil) : lhs.value! == rhs.value!) &&
        true
    )
}
