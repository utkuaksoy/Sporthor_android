//
//  BaseViewController.swift
//  Sporthor
//
//  Created by derTurke on 29.01.2025.
//

import UIKit
import Kingfisher

class BaseViewController: UIViewController {
    // MARK: - VIPER Variables
    var basePresenter: BasePresenterProtocol?
    
    // MARK: - Members
    
    
    // MARK: - Lifecycles
    override func viewDidLoad() {
        super.viewDidLoad()
        basePresenter?.baseViewDidLoad()
        print("ViewController Init: " + String(describing: Self.self))
    }
    
    // MARK: - Custom Methods
}

// MARK: - BasePresenterDelegate
extension BaseViewController: BasePresenterDelegate {
    @objc func didSetBackgroundColor(_ color: UIColor) {
        view.backgroundColor = .white
    }
    
    func didSetTitle(_ title: String) {
        navigationItem.title = title
    }
}
