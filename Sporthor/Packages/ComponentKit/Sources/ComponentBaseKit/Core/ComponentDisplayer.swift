//
//  ComponentDisplayer.swift
//  
//
//  Created by Mesut Canbaz on 10.02.2025.
//

import UIKit

public protocol ComponentDisplayer {
    func willAppear(at indexPath: IndexPath)
    func willDisappear(at indexPath: IndexPath)
}

public extension ComponentDisplayer {
    func willAppear(at indexPath: IndexPath) {}
    func willDisappear(at indexPath: IndexPath) {}
}
