import Foundation
import SwiftyJSON

public struct WithCustomIntWrapper: Serializable {
    
    public let wrapper: CustomInt?
    
    public init(
        wrapper: CustomInt?
    ) {
        self.wrapper = wrapper
    }
    
    public static func readJSON(json: JSON) throws -> WithCustomIntWrapper {
        return WithCustomIntWrapper(
            wrapper: try json["wrapper"].optional(.Number).int.map { try CustomIntCoercer.coerceInput($0) }
        )
    }
    public func writeData() -> [String: AnyObject] {
        var dict: [String : AnyObject] = [:]
        if let wrapper = self.wrapper {
            dict["wrapper"] = CustomIntCoercer.coerceOutput(wrapper)
        }
        return dict
    }
}
