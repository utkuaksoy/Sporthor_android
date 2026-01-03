//
//  SplashViewController.swift
//  Sporthor
//
//  Created by derTurke on 30.01.2025.
//
//

import UIKit

final class SplashViewController: BaseViewController {
    // MARK: - VIPER Variables
    var presenter: SplashPresenterProtocol {
        get { return self.basePresenter as! SplashPresenterProtocol }
        set { self.basePresenter = newValue }
    }
    
    // MARK: - UI Elements
    private(set) lazy var imageView: UIImageView = {
        let imageView = UIImageView()
        imageView.image = UIImage(named: "splash-logo")
        imageView.translatesAutoresizingMaskIntoConstraints = false
        return imageView
    }()
    
    // MARK: - Members
    
    // MARK: - Lifecycles
    override func viewDidLoad() {
        super.viewDidLoad()
        presenter.viewDidLoad()
    }
    
    // MARK: - Custom Methods
}

// MARK: - SplashPresenterDelegate
extension SplashViewController: SplashPresenterDelegate {
    func prepareUI() {
        view.addSubview(imageView)
        
        NSLayoutConstraint.activate([
            imageView.centerXAnchor.constraint(equalTo: view.centerXAnchor),
            imageView.centerYAnchor.constraint(equalTo: view.centerYAnchor),
            imageView.heightAnchor.constraint(equalToConstant: 50)
        ])
    }
}
