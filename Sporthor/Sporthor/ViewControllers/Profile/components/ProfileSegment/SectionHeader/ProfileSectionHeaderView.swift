//
//  ProfileSectionHeaderView.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 13.02.2025.
//

import ComponentKit
import ComponentBaseKit
import DesignKit
import UIKit

protocol SectionHeaderDelegate: AnyObject {
    func didSelectTab(at item: SegmentItem)
}

final class ProfileSectionHeaderView: UICollectionReusableView, ReusableView {
    
    // MARK: - Private UI Elements
    
    private lazy var stackView: UIStackView = {
        let stackView = UIStackView(arrangedSubviews: [indicatorView, collectionView])
        stackView.axis = .vertical
        stackView.spacing = 6
        stackView.alignment = .fill
        stackView.distribution = .fill
        stackView.translatesAutoresizingMaskIntoConstraints = false
        return stackView
    }()
    
    private lazy var indicatorView: UIView = {
        let view = UIView()
        view.backgroundColor = ColorName.borderSoft200.color
        view.translatesAutoresizingMaskIntoConstraints = false
        return view
    }()
    
    private lazy var collectionView: UICollectionView = {
        let layout = UICollectionViewFlowLayout()
        layout.scrollDirection = .horizontal
        layout.minimumInteritemSpacing = .zero
        layout.minimumLineSpacing = .zero
        let collectionView = UICollectionView(frame: .zero, collectionViewLayout: layout)
        collectionView.showsHorizontalScrollIndicator = false
        collectionView.backgroundColor = .clear
        collectionView.translatesAutoresizingMaskIntoConstraints = false
        collectionView.delegate = self
        collectionView.dataSource = self
        collectionView.register(ProfileTabItemCell.self, forCellWithReuseIdentifier: ProfileTabItemCell.reuseIdentifier)
        return collectionView
    }()
    
    // MARK: - Private Properties
    
    private var viewModel: ProfileSectionHeaderViewModel?
    private weak var delegate: SectionHeaderDelegate?
    
    override init(frame: CGRect) {
        super.init(frame: frame)
        setupViews()
        setupConstraints()
    }
    
    required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
    }
    
    func configure(delegate: SectionHeaderDelegate, viewModel: ProfileSectionHeaderViewModel) {
        self.delegate = delegate
        self.viewModel = viewModel
        collectionView.reloadData()
    }
}

// MARK: - UICollectionViewDataSource & UICollectionViewDelegate

extension ProfileSectionHeaderView: UICollectionViewDataSource, UICollectionViewDelegateFlowLayout {
    
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return viewModel?.numberOfItemsInSection() ?? .zero
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        guard let cell = collectionView.dequeueReusableCell(
            withReuseIdentifier: ProfileTabItemCell.reuseIdentifier,
            for: indexPath
        ) as? ProfileTabItemCell else {
            return UICollectionViewCell()
        }
        
        let isSelected = indexPath.item == viewModel?.selectedTabIndex
        cell.configure(
            title: viewModel?.getTitle(indexPath: indexPath),
            image: viewModel?.getImage(indexPath: indexPath),
            isSelected: isSelected
        )
        
        return cell
    }
    
    func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        guard let viewModel, viewModel.selectedTabIndex != indexPath.item else { return }
        
        let previousSelectedIndex = viewModel.selectedTabIndex
        viewModel.selectedTabIndex = indexPath.item
        
        let indexPathsToReload = [IndexPath(item: previousSelectedIndex, section: 0), indexPath]
        collectionView.reloadItems(at: indexPathsToReload)
        
        if let selectedTab = viewModel.tabs?[safe: indexPath.item] {
            delegate?.didSelectTab(at: selectedTab)
        }
    }
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
        guard let viewModel, let tabs = viewModel.tabs, !tabs.isEmpty else { return .zero }
//        if tabs.count == 1 {
//            guard let title = viewModel.getTitle(indexPath: indexPath) else { return .zero }
//            let width = title.width(withConstrainedHeight: collectionView.frame.height, font: .bold04Compact)
//            return CGSize(width: width + 58, height: collectionView.frame.height)
//        }
//        let width = ((collectionView.frame.size.width) / CGFloat(tabs.count))
//        return CGSize(width: width, height: collectionView.frame.height)
        guard let title = viewModel.getTitle(indexPath: indexPath) else { return .zero }
        let width = title.width(withConstrainedHeight: collectionView.frame.height, font: .bold04Compact)
        return CGSize(width: width + 58, height: collectionView.frame.height)
    }
}

// MARK: - Setup

private extension ProfileSectionHeaderView {
    func setupViews() {
        self.backgroundColor = .white
        addSubview(stackView)
    }
    
    func setupConstraints() {
        NSLayoutConstraint.activate([
            stackView.leadingAnchor.constraint(equalTo: leadingAnchor),
            stackView.trailingAnchor.constraint(equalTo: trailingAnchor),
            stackView.topAnchor.constraint(equalTo: topAnchor, constant: 16),
            stackView.bottomAnchor.constraint(equalTo: bottomAnchor),
            
            indicatorView.heightAnchor.constraint(equalToConstant: 1)
        ])
    }
}
