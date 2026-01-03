//
//  TournamentsContainerCell.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 20.03.2025.
//

import ComponentKit
import ComponentBaseKit
import DesignKit
import UIKit

final class TournamentsContainerCell: UICollectionViewCell, ReusableView {
    
    // MARK: - Private UI Elements
    
    private let containerView: UIView = {
        let view = UIView()
        view.backgroundColor = .white
        view.translatesAutoresizingMaskIntoConstraints = false
        return view
    }()
    
    private let titleLabelContainerView: UIView = {
        let view = UIView()
        view.backgroundColor = .clear
        view.translatesAutoresizingMaskIntoConstraints = false
        return view
    }()
    
    private lazy var titleLabel: CKLabel = {
        let label = CKLabel(
            textColor: ColorName.contentStrong900.color,
            font: .bold03Compact
        )
        label.translatesAutoresizingMaskIntoConstraints = false
        return label
    }()
    
    private lazy var containerStackView: CKStackView = {
        let stackView = CKStackView(
            axis: .vertical,
            distribution: .fill,
            alignment: .fill,
            spacing: 16
        )
        stackView.addArrangedSubviews([titleLabelContainerView, collectionView])
        stackView.translatesAutoresizingMaskIntoConstraints = false
        return stackView
    }()
    
    private lazy var collectionView: UICollectionView = {
        let layout = UICollectionViewFlowLayout()
        layout.scrollDirection = .vertical
        layout.minimumLineSpacing = 16
        let collectionView = UICollectionView(frame: .zero, collectionViewLayout: layout)
        collectionView.backgroundColor = .white
        collectionView.register(
            TournamentItemCell.self,
            forCellWithReuseIdentifier: TournamentItemCell.reuseIdentifier
        )
        collectionView.dataSource = self
        collectionView.delegate = self
        collectionView.showsHorizontalScrollIndicator = false
        collectionView.translatesAutoresizingMaskIntoConstraints = false
        return collectionView
    }()
    
    // MARK: - Private Properties
    
    private var data: TournamentDataModel?
    
    // MARK: - Initializers
    
    override init(frame: CGRect) {
        super.init(frame: frame)
        setupViews()
        setupConstraints()
    }
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    // MARK: - Configure
    
    func configure(
        with data: TournamentDataModel,
        at indexPath: IndexPath
    ) {
        self.data = data
        configureTitleLabel(with: data.title)
        collectionView.reloadData()
    }
    
    private func configureTitleLabel(with name: String?) {
        titleLabel.text = name
    }
}

    // MARK: UICollectionView Delegate&Datasource

    extension TournamentsContainerCell: UICollectionViewDataSource, UICollectionViewDelegate, UICollectionViewDelegateFlowLayout {
        func collectionView(
            _ collectionView: UICollectionView,
            numberOfItemsInSection section: Int
        ) -> Int {
            return data?.tournaments?.count ?? .zero
        }
        
        func collectionView(
            _ collectionView: UICollectionView,
            cellForItemAt indexPath: IndexPath
        ) -> UICollectionViewCell {
            guard let cell = collectionView.dequeueReusableCell(withReuseIdentifier: TournamentItemCell.reuseIdentifier, for: indexPath) as? TournamentItemCell,
                  let item = data?.tournaments?[safe: indexPath.row] else { return UICollectionViewCell() }
            cell.configure(with: item)
            return cell
        }
        
        func collectionView(
            _ collectionView: UICollectionView,
            layout collectionViewLayout: UICollectionViewLayout,
            sizeForItemAt indexPath: IndexPath
        ) -> CGSize {
            return CGSize(width: collectionView.frame.size.width, height: 56)
        }
    }

    // MARK: - Setup

    private extension TournamentsContainerCell {
        func setupViews() {
            contentView.addSubview(containerView)
            titleLabelContainerView.addSubview(titleLabel)
            containerView.addSubview(containerStackView)
        }
        
        func setupConstraints() {
            NSLayoutConstraint.activate([
                containerView.leadingAnchor.constraint(equalTo: contentView.leadingAnchor, constant: 16),
                containerView.trailingAnchor.constraint(equalTo: contentView.trailingAnchor, constant: -16),
                containerView.topAnchor.constraint(equalTo: contentView.topAnchor, constant: 24),
                containerView.bottomAnchor.constraint(equalTo: contentView.bottomAnchor),
                
                containerStackView.leadingAnchor.constraint(equalTo: containerView.leadingAnchor),
                containerStackView.trailingAnchor.constraint(equalTo: containerView.trailingAnchor),
                containerStackView.topAnchor.constraint(equalTo: containerView.topAnchor),
                containerStackView.bottomAnchor.constraint(equalTo: containerView.bottomAnchor),
                
                titleLabelContainerView.heightAnchor.constraint(equalToConstant: 20),
                titleLabel.leadingAnchor.constraint(equalTo: titleLabelContainerView.leadingAnchor),
                titleLabel.trailingAnchor.constraint(equalTo: titleLabelContainerView.trailingAnchor),
                titleLabel.topAnchor.constraint(equalTo: titleLabelContainerView.topAnchor),
                titleLabel.bottomAnchor.constraint(equalTo: titleLabelContainerView.bottomAnchor),
            ])
        }
    }

