import Foundation
import SwiftyJSON

public struct WithCustomTypesMap: JSONSerializable, Equatable {
    
    public let ints: [String: Int]?
    
    public init(
        ints: [String: Int]?
    ) {
        self.ints = ints
    }
    
    public static func read(json: JSON) -> WithCustomTypesMap {
        return WithCustomTypesMap(
            ints: json["ints"].dictionary.map { $0.mapValues { $0.intValue } }
        )
    }
    public func write() -> JSON {
        var json: [String : JSON] = [:]
        if let ints = self.ints {
            json["ints"] = JSON(ints)
        }
        return JSON(json)
    }
}
public func ==(lhs: WithCustomTypesMap, rhs: WithCustomTypesMap) -> Bool {
    return (
        (lhs.ints == nil ? (rhs.ints == nil) : lhs.ints! == rhs.ints!) &&
        true
    )
}
