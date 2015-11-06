import Foundation
import SwiftyJSON

public struct WithCustomIntWrapper: Serializable {
    
    public let wrapper: Int?
    
    public init(
        wrapper: Int?
    ) {
        self.wrapper = wrapper
    }
    
    public static func readJSON(json: JSON) throws -> WithCustomIntWrapper {
        return WithCustomIntWrapper(
            wrapper: json["wrapper"].int
        )
    }
    public func writeData() -> [String: AnyObject] {
        var dict: [String : AnyObject] = [:]
        if let wrapper = self.wrapper {
            dict["wrapper"] = wrapper
        }
        return dict
    }
}
