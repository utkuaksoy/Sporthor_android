//
//  MatchesCell.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 15.03.2025.
//

import ComponentKit
import ComponentBaseKit
import DesignKit
import UIKit
import Kingfisher

final class MatchesCell: UICollectionViewCell, ReusableView {
    
    // MARK: - Private UI Elements
    
    private lazy var containerView: UIView = {
        let view = UIView()
        view.backgroundColor = .white
        view.translatesAutoresizingMaskIntoConstraints = false
        return view
    }()
    
    private  lazy var containerStackView: UIStackView = {
        let stack = UIStackView(
            arrangedSubviews: [
                leagueInfoView,
                matchStackView,
                seperatorView
            ]
        )
        stack.axis = .vertical
        stack.spacing = 16
        stack.distribution = .fill
        stack.translatesAutoresizingMaskIntoConstraints = false
        return stack
    }()
    
    private lazy var matchStackView: CKStackView = {
        let view = CKStackView(
            axis: .horizontal,
            distribution: .fill,
            alignment: .fill,
            spacing: 16
        )
        view.addArrangedSubviews(
            [
                teamsStackView,
                horizontalSeperatorView,
                arrowImageContainerView
            ]
        )
        view.translatesAutoresizingMaskIntoConstraints = false
        return view
    }()
    
    private lazy var leagueInfoView: MatchesLeagueInfoView = {
        let view = MatchesLeagueInfoView()
        view.translatesAutoresizingMaskIntoConstraints = false
        return view
    }()
    
    private lazy var homeTeamView = MatchesTeamView()
    private lazy var awayTeamView = MatchesTeamView()
    
    private lazy var teamsStackView: UIStackView = {
        let stack = CKStackView(
            axis: .vertical,
            distribution: .fillEqually,
            alignment: .fill,
            spacing: .zero
        )
        stack.addArrangedSubviews([homeTeamView, awayTeamView])
        stack.translatesAutoresizingMaskIntoConstraints = false
        return stack
    }()
    
    private lazy var arrowImageContainerView: UIView = {
        let view = UIView()
        view.backgroundColor = .white
        view.translatesAutoresizingMaskIntoConstraints = false
        return view
    }()
    
    private let arrowImageView: UIImageView = {
        let imageView = UIImageView()
        imageView.image = UIImage(systemName: "chevron.right")
        imageView.tintColor = ColorName.contentStrong900.color
        imageView.translatesAutoresizingMaskIntoConstraints = false
        return imageView
    }()
    
    private lazy var seperatorView: UIView = {
        let view = UIView()
        view.backgroundColor = ColorName.borderSoft200.color
        view.translatesAutoresizingMaskIntoConstraints = false
        return view
    }()
    
    private lazy var horizontalSeperatorView: UIView = {
        let view = UIView()
        view.backgroundColor = ColorName.borderSoft200.color
        view.translatesAutoresizingMaskIntoConstraints = false
        return view
    }()
    
    // MARK: - Initializer
    
    override init(frame: CGRect) {
        super.init(frame: frame)
        setupViews()
        setupConstraints()
    }
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    // MARK: - Configure Methods
    
    func configure(with match: MatchModel?) {
        guard let match = match else { return }
        
        if let leagueInfo = match.leagueInfo {
            leagueInfoView.configure(with: leagueInfo)
        }
        
        for (index, team) in match.teams.enumerated() {
            if index == 0 {
                homeTeamView.configure(with: team)
            } else if index == 1 {
                awayTeamView.configure(with: team)
            }
        }
    }
}

// MARK: - Setup

private extension MatchesCell {
    
    func setupViews() {
        contentView.addSubview(containerView)
        containerView.addSubview(containerStackView)
        arrowImageContainerView.addSubview(arrowImageView)
    }
    
    func setupConstraints() {
        NSLayoutConstraint.activate([
            containerView.leadingAnchor.constraint(equalTo: contentView.leadingAnchor, constant: 16),
            containerView.trailingAnchor.constraint(equalTo: contentView.trailingAnchor, constant: -16),
            containerView.topAnchor.constraint(equalTo: contentView.topAnchor, constant: 20),
            containerView.bottomAnchor.constraint(equalTo: contentView.bottomAnchor),
            
            containerStackView.leadingAnchor.constraint(equalTo: containerView.leadingAnchor),
            containerStackView.trailingAnchor.constraint(equalTo: containerView.trailingAnchor),
            containerStackView.topAnchor.constraint(equalTo: containerView.topAnchor),
            containerStackView.bottomAnchor.constraint(equalTo: containerView.bottomAnchor),
            
            leagueInfoView.heightAnchor.constraint(equalToConstant: 18),
            
            seperatorView.heightAnchor.constraint(equalToConstant: 1),
            horizontalSeperatorView.widthAnchor.constraint(equalToConstant: 1),
            
            arrowImageContainerView.widthAnchor.constraint(equalToConstant: 60),
            
            arrowImageView.centerYAnchor.constraint(equalTo: arrowImageContainerView.centerYAnchor),
            arrowImageView.centerXAnchor.constraint(equalTo: arrowImageContainerView.centerXAnchor),
            arrowImageView.widthAnchor.constraint(equalToConstant: 24),
            arrowImageView.heightAnchor.constraint(equalToConstant: 24),
            
        ])
    }
}
