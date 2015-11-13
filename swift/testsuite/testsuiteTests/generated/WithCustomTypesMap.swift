import Foundation
import SwiftyJSON

public struct WithCustomTypesMap: Serializable, Equatable {
    
    public let ints: [String: CustomInt]?
    
    public init(
        ints: [String: CustomInt]?
    ) {
        self.ints = ints
    }
    
    public static func readJSON(json: JSON) throws -> WithCustomTypesMap {
        return WithCustomTypesMap(
            ints: try json["ints"].optional(.Dictionary).dictionary.map { try $0.mapValues { try CustomIntCoercer.coerceInput(try $0.required(.Number).intValue) } }
        )
    }
    public func writeData() -> [String: AnyObject] {
        var dict: [String : AnyObject] = [:]
        if let ints = self.ints {
            dict["ints"] = ints.mapValues { CustomIntCoercer.coerceOutput($0) }
        }
        return dict
    }
}
public func ==(lhs: WithCustomTypesMap, rhs: WithCustomTypesMap) -> Bool {
    return (
        (lhs.ints == nil ? (rhs.ints == nil) : lhs.ints! == rhs.ints!) &&
        true
    )
}
