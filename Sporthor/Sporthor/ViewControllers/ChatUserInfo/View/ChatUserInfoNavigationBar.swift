//
//  ChatUserInfoNavigationBar.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 15.04.2025.
//
//

import UIKit
import DesignKit

protocol ChatUserInfoNavigationBarDelegate: AnyObject {
    func didTapBackButton()
}

final class ChatUserInfoNavigationBar: UIView {
    
    // MARK: - Private UI Elements
    private lazy var titleLabel: UILabel = {
        let label = UILabel()
        label.text = "Kişi Bilgisi"
        label.font = .bold03Compact
        label.textColor = ColorName.contentStrong900.color
        label.translatesAutoresizingMaskIntoConstraints = false
        return label
    }()
    
    lazy var backButton: UIBarButtonItem = {
        let backImage = Asset.chevronLeftIcon.image.withRenderingMode(.alwaysOriginal)
        return UIBarButtonItem(
            image: backImage,
            style: .plain,
            target: self,
            action: #selector(didTapBackButton)
        )
    }()
    
    // MARK: - Properties
    weak var delegate: ChatUserInfoNavigationBarDelegate?
    
    // MARK: - Initialization

    init(delegate: ChatUserInfoNavigationBarDelegate) {
        self.delegate = delegate
        super.init(frame: .zero)
        setupUI()
    }
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    // MARK: - Setup
    private func setupUI() {
        addSubview(titleLabel)
        
        NSLayoutConstraint.activate([
            titleLabel.centerXAnchor.constraint(equalTo: centerXAnchor),
            titleLabel.centerYAnchor.constraint(equalTo: centerYAnchor)
        ])
    }
    
    // MARK: - Actions
    @objc
    private func didTapBackButton() {
        delegate?.didTapBackButton()
    }
} 
