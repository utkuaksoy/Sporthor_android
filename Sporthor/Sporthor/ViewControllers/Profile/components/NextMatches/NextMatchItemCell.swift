//
//  NextMatchItemCell.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 11.02.2025.
//

import ComponentKit
import ComponentBaseKit
import DesignKit
import UIKit

final class NextMatchItemCell: UICollectionViewCell, ReusableView {
    
    // MARK: - Private UI Elements
    
    private lazy var containerView: UIView = {
        let view = UIView()
        view.backgroundColor = DesignKitColorName.backgroundWeak100.color
        view.layer.cornerRadius = 8
        view.layer.borderWidth = 1
        view.layer.borderColor = DesignKitColorName.borderSoft200.color.cgColor
        view.translatesAutoresizingMaskIntoConstraints = false
        return view
    }()
    
    private lazy var containerStackView: UIStackView = {
        let stackView = UIStackView(
            arrangedSubviews: [
                teamStackView,
                arrowImageView
            ]
        )
        stackView.axis = .horizontal
        stackView.spacing = 12
        stackView.alignment = .center
        stackView.distribution = .fill
        stackView.translatesAutoresizingMaskIntoConstraints = false
        return stackView
    }()
    
    private lazy var teamStackView: UIStackView = {
        let stackView = UIStackView()
        stackView.axis = .vertical
        stackView.spacing = 6
        stackView.alignment = .leading
        stackView.translatesAutoresizingMaskIntoConstraints = false
        return stackView
    }()
    
    private lazy var arrowImageView: UIImageView = {
        let imageView = UIImageView()
        imageView.contentMode = .scaleAspectFit
        imageView.image = UIImage(systemName: "chevron.right")
        imageView.tintColor = DesignKitColorName.contentStrong900.color
        imageView.translatesAutoresizingMaskIntoConstraints = false
        return imageView
    }()
    
    // MARK: - Private Properties
    
    private var teamViews: [UIView] = []
    
    // MARK: - Initializer

    override init(frame: CGRect) {
        super.init(frame: frame)
        setupViews()
        setupConstraints()
    }
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    // MARK: - Configuration

    func configure(with item: MatchModel?) {
        configureTeams(teams: item?.teams)
    }
    
    private func configureTeams(teams: [MatchTeamModel]?) {
        teamStackView.arrangedSubviews.forEach { $0.removeFromSuperview() }
        guard let teams = teams, !teams.isEmpty else {
            teamStackView.isHidden = true
            return
        }
        
        teamStackView.isHidden = false
        
        for team in teams {
            let teamView = createTeamView(
                name: team.teamName,
                logoURL: team.teamLogoImageUrl
            )
            teamStackView.addArrangedSubview(teamView)
        }
    }
    
    private func createTeamView(name: String?, logoURL: String?) -> UIView {
        let teamImageView: UIImageView = {
            let imageView = UIImageView()
            imageView.contentMode = .scaleAspectFit
            imageView.layer.cornerRadius = imageView.frame.width / 2
            imageView.clipsToBounds = true
            imageView.translatesAutoresizingMaskIntoConstraints = false
            return imageView
        }()
        
        let teamLabel: UILabel = {
            let label = UILabel()
            label.font = .body05MediumCompact
            label.textColor = DesignKitColorName.contentStrong900.color
            label.text = name
            label.translatesAutoresizingMaskIntoConstraints = false
            return label
        }()
        
        let teamStackView: UIStackView = {
            let stackView = UIStackView()
            stackView.axis = .horizontal
            stackView.spacing = 8
            stackView.alignment = .center
            stackView.translatesAutoresizingMaskIntoConstraints = false
            stackView.addArrangedSubview(teamImageView)
            stackView.addArrangedSubview(teamLabel)
            return stackView
        }()
        
        teamImageView.setImage(with: logoURL)
        
        NSLayoutConstraint.activate([
            teamImageView.widthAnchor.constraint(equalToConstant: 32),
            teamImageView.heightAnchor.constraint(equalToConstant: 32)
        ])
        
        return teamStackView
    }
}

// MARK: - Setup

private extension NextMatchItemCell {
    func setupViews() {
        contentView.addSubview(containerView)
        containerView.addSubview(containerStackView)
    }
    
    func setupConstraints() {
        NSLayoutConstraint.activate([
            containerView.leadingAnchor.constraint(equalTo: contentView.leadingAnchor),
            containerView.trailingAnchor.constraint(equalTo: contentView.trailingAnchor),
            containerView.topAnchor.constraint(equalTo: contentView.topAnchor),
            containerView.bottomAnchor.constraint(equalTo: contentView.bottomAnchor),
            
            containerStackView.leadingAnchor.constraint(equalTo: containerView.leadingAnchor, constant: 12),
            containerStackView.trailingAnchor.constraint(equalTo: containerView.trailingAnchor, constant: -12),
            containerStackView.topAnchor.constraint(equalTo: containerView.topAnchor, constant: 8),
            containerStackView.bottomAnchor.constraint(equalTo: containerView.bottomAnchor, constant: -8),
            
            arrowImageView.widthAnchor.constraint(equalToConstant: 24),
            arrowImageView.heightAnchor.constraint(equalToConstant: 24)
        ])
    }
}
