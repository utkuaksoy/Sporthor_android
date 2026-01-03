//
//  ProfileLoadingView.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 20.03.2025.
//

import ComponentBaseKit
import UIKit

final class ProfileLoadingView: UIView {
    private let activityIndicator = UIActivityIndicatorView(style: .medium)
    
    override init(frame: CGRect) {
        super.init(frame: frame)
        setupUI()
    }
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    private func setupUI() {
        backgroundColor = .white
        
        addSubview(activityIndicator)
        activityIndicator.translatesAutoresizingMaskIntoConstraints = false
        activityIndicator.style = .large
        activityIndicator.color = .systemGray
        activityIndicator.hidesWhenStopped = false
        
        activityIndicator.transform = CGAffineTransform(scaleX: 1.5, y: 1.5)
        
        NSLayoutConstraint.activate([
            activityIndicator.centerXAnchor.constraint(equalTo: centerXAnchor),
            activityIndicator.centerYAnchor.constraint(equalTo: centerYAnchor)
        ])
        
        layer.cornerRadius = 10
        clipsToBounds = true
    }
    
    override func layoutSubviews() {
        super.layoutSubviews()
        print("Loading view frame: \(frame)")
    }
    
    func startLoading() {
        isHidden = false
        alpha = 1
        activityIndicator.startAnimating()
    }
    
    func stopLoading() {
        UIView.animate(withDuration: 0.2, animations: {
            self.alpha = 0
        }) { _ in
            self.activityIndicator.stopAnimating()
            self.isHidden = true
            self.removeFromSuperview()
        }
    }
    
    func checkVisibility() {
        print("Loading view visibility check:")
        print("- isHidden: \(isHidden)")
        print("- alpha: \(alpha)")
        print("- frame: \(frame)")
        print("- superview: \(superview != nil)")
        print("- window: \(window != nil)")
        print("- activityIndicator.isAnimating: \(activityIndicator.isAnimating)")
    }
} 
