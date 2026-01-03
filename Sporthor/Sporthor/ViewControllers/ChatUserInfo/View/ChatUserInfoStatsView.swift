//
//  ChatUserInfoStatsView.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 15.04.2025.
//
//

import UIKit
import DesignKit

final class ChatUserInfoStatsView: UIView {

    // MARK: - Private UI Elements

    private lazy var stackView: UIStackView = {
        let stackView = UIStackView()
        stackView.axis = .horizontal
        stackView.distribution = .equalSpacing
        stackView.alignment = .center
        stackView.spacing = 16
        stackView.translatesAutoresizingMaskIntoConstraints = false
        return stackView
    }()
    
    private lazy var mediaCountLabel: UILabel = {
        let label = UILabel()
        label.font = .bold04Compact
        label.textColor = .black
        label.textAlignment = .center
        label.numberOfLines = 2
        label.translatesAutoresizingMaskIntoConstraints = false
        return label
    }()
    
    private lazy var followersLabel: UILabel = {
        let label = UILabel()
        label.font = .bold04Compact
        label.textColor = .black
        label.textAlignment = .center
        label.numberOfLines = 2
        label.translatesAutoresizingMaskIntoConstraints = false
        return label
    }()
    
    private lazy var followingLabel: UILabel = {
        let label = UILabel()
        label.font = .bold04Compact
        label.textColor = .black
        label.textAlignment = .center
        label.numberOfLines = 2
        label.translatesAutoresizingMaskIntoConstraints = false
        return label
    }()
    
    // MARK: - Initialization
    override init(frame: CGRect) {
        super.init(frame: frame)
        setupUI()
    }
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    // MARK: - Setup
    private func setupUI() {
        addSubview(stackView)
        stackView.addArrangedSubview(mediaCountLabel)
        stackView.addArrangedSubview(followersLabel)
        stackView.addArrangedSubview(followingLabel)
        
        NSLayoutConstraint.activate([
            stackView.topAnchor.constraint(equalTo: topAnchor),
            stackView.leadingAnchor.constraint(equalTo: leadingAnchor),
            stackView.trailingAnchor.constraint(equalTo: trailingAnchor),
            stackView.bottomAnchor.constraint(equalTo: bottomAnchor)
        ])
    }
    
    // MARK: - Configuration

    func configure(with viewModel: ChatUserInfoResponse) {
        mediaCountLabel.text = "\(viewModel.mediaCount) Gönderi"
        followersLabel.text = "\(viewModel.followersCount) Takipçi"
        followingLabel.text = "\(viewModel.followingCount) Takip"
    }
} 
