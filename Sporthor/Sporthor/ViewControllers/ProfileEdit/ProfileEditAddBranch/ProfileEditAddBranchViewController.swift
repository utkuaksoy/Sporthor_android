//
//  ProfileEditAddBranchViewController.swift
//  Sporthor
//
//  Created by derTurke on 19.04.2025.
//
//

import UIKit
import ComponentKit

final class ProfileEditAddBranchViewController: BaseViewController {
    // MARK: - VIPER Variables
    var presenter: ProfileEditAddBranchPresenterProtocol {
        get { return self.basePresenter as! ProfileEditAddBranchPresenterProtocol }
        set { self.basePresenter = newValue }
    }
    
    // MARK: - UI Elements
    private lazy var collectionView: UICollectionView = {
        let collectionView = UICollectionView(frame: .zero,
                                              collectionViewLayout: UICollectionViewFlowLayout())
        collectionView.delegate = self
        collectionView.dataSource = self
        collectionView.translatesAutoresizingMaskIntoConstraints = false
        collectionView.contentInset = UIEdgeInsets(top: 32, left: 0, bottom: 0, right: 0)
        collectionView.backgroundColor = .clear
        return collectionView
    }()
    
    private lazy var continueButton: CKButton = {
        let button = CKButton(
            delegate: self,
            title: "Ekle",
            titleColor: DesignKitColorName.contentStrong900.color,
            buttonBackgroundColor: DesignKitColorName.backgroundPrimaryGreen.color,
            cornerRadius: 23,
            font: .bold03Compact)
        button.translatesAutoresizingMaskIntoConstraints = false
        return button
    }()
    
    // MARK: - Members
    
    // MARK: - Lifecycles
    override func viewDidLoad() {
        super.viewDidLoad()
        presenter.viewDidLoad()
    }
    
    // MARK: - Custom Methods
}

// MARK: - ProfileEditAddBranchPresenterDelegate
extension ProfileEditAddBranchViewController: ProfileEditAddBranchPresenterDelegate {
    func prepareNavigationBar() {
        if let navCon = navigationController as? CustomNavigationController {
            navCon.isCloseExist = true
            navCon.customDelegate = self
        }
    }
    
    func prepareUI() {
        view.addSubview(collectionView)
        view.addSubview(continueButton)
        
        NSLayoutConstraint.activate([
            collectionView.topAnchor.constraint(equalTo: view.safeAreaLayoutGuide.topAnchor),
            collectionView.leadingAnchor.constraint(equalTo: view.leadingAnchor),
            collectionView.trailingAnchor.constraint(equalTo: view.trailingAnchor),
            continueButton.topAnchor.constraint(equalTo: collectionView.bottomAnchor, constant: 16),
            continueButton.bottomAnchor.constraint(equalTo: view.safeAreaLayoutGuide.bottomAnchor, constant: -32),
            continueButton.leadingAnchor.constraint(equalTo: view.leadingAnchor, constant: 24),
            continueButton.trailingAnchor.constraint(equalTo: view.trailingAnchor, constant: -24),
            continueButton.heightAnchor.constraint(equalToConstant: 46)
        ])
    }
    
    func reloadData() {
        DispatchQueue.main.async { [weak self] in
            guard let self else { return }
            self.collectionView.reloadData()
        }
    }
}

// MARK: - UICollectionViewDataSource
extension ProfileEditAddBranchViewController: UICollectionViewDataSource {
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return presenter.model.count
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let cell = RectangleImageTitleCollectionViewCell.dequeue(from: collectionView, at: indexPath)
        cell.bind(model: presenter.model[indexPath.item])
        return cell
    }
    
    func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        presenter.didSelectItemAt(indexPath)
    }
}

// MARK: - UICollectionViewDelegateFlowLayout
extension ProfileEditAddBranchViewController: UICollectionViewDelegateFlowLayout {
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
        return CGSize(width: (collectionView.frame.width - 62) / 2,
                      height: 140)
    }
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, insetForSectionAt section: Int) -> UIEdgeInsets {
        return UIEdgeInsets(top: 0, left: 24, bottom: 0, right: 24)
    }
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, minimumLineSpacingForSectionAt section: Int) -> CGFloat {
        return 14
    }
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, minimumInteritemSpacingForSectionAt section: Int) -> CGFloat {
        return 14
    }
}

// MARK: - CKButtonDelegate
extension ProfileEditAddBranchViewController: CKButtonDelegate {
    func ckButtonDidTap(tag: Int) {
        presenter.didTappedContinueButton()
    }
}

// MARK: - CustomNavigationControllerDelegate
extension ProfileEditAddBranchViewController: CustomNavigationControllerDelegate {
    func didTapButton(type: BarButtonItemType) {
        switch type {
        case .close:
            presenter.didTappedCloseButton()
        default:
            break
        }
    }
}
