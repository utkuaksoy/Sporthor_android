//
//  GenericComponentDelegates.swift
//  
//
//  Created by Mesut Canbaz on 10.02.2025.
//

import UIKit

public protocol ComponentDeeplinkDelegate: AnyObject {
    func componentDidTriggerDeeplink(_ deeplink: URL)
}

public protocol ComponentInvalidateDelegate: AnyObject {
    func componentDidInvalidateSection(_ section: Int)
}
